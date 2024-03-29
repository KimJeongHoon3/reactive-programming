package com.study.reactiveprgramming.etc;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class NioTest {
    public static void splitFileIntoDir(String srcFilePath,
                                        String destDirPath,
                                        String splittedFileNameFormat,
                                        String header,
                                        ByteBuffer buffer) throws IOException {

        final byte LINE_FEED = 0x0A;
        final byte CARRIAGE_RETURN = 0x0D;

        int fileCounter = 0;
        long totalReadBytes = 0L;
        long totalWriteBytes = 0L;
        long readBytes;

        final Path path = Paths.get(srcFilePath);

        try (final FileChannel srcFileChannel = FileChannel.open(path, StandardOpenOption.READ)) {
            while ((readBytes = srcFileChannel.read(buffer)) >= 0) {
                totalReadBytes += readBytes;

                final int contentLength = buffer.position();
                int newLinePosition = buffer.position();

                try (final FileChannel splittedFileChannel =
                             FileChannel.open(Paths.get(destDirPath, String.format(splittedFileNameFormat, ++fileCounter)),
                                     StandardOpenOption.TRUNCATE_EXISTING,
                                     StandardOpenOption.CREATE,
                                     StandardOpenOption.WRITE)) {
                    writeHeader(header, readBytes, splittedFileChannel);

                    boolean hasLineFeed = false;
                    boolean needCompact = true;
                    while (newLinePosition > 0) {
                        if (buffer.get(--newLinePosition) == LINE_FEED) {  // 1 byte 씩 뒤로 가면서 줄바꿈 탐색
                            if (newLinePosition + 1 == buffer.capacity()) {  // 버퍼 끝에 줄바꿈이 있으면 compact 불필요
                                needCompact = false;
                            }
                            buffer.position(0);  // buffer의 처음부터
                            buffer.limit(++newLinePosition);  // LINE_FEED 까지 포함해서 write 되도록 limit 조정
                            // 버퍼의 [0, limit)의 내용을 splittedFileChannel이 바인딩된 파일에 write
                            totalWriteBytes += splittedFileChannel.write(buffer);
                            splittedFileChannel.close();
                            hasLineFeed = true;
                            break;
                        }
                    }

                    if (!hasLineFeed) {
                        throw new IllegalArgumentException("버퍼 안에 줄바꿈이 없습니다. 버퍼 크기는 한 행의 길이보다 커야 합니다.");
                    }

                    if (needCompact) {
                        // compact()를 위해 원래 읽었던 내용의 마지막 바이트 위치+1(==contentLength) 로 limit 설정
                        buffer.limit(contentLength);

                        // 버퍼의 [position, limit) 의 내용을 [0, limit - position) 으로 복사
                        buffer.compact();
                        // 복사 후 position은 limit에 위치하며 다음에 파일에서 읽어오는 내용이 position 부터 이어짐
                        // limit는 capacity로 이동
                    } else {
                        // compact()가 필요없다면 파일을 읽어서 버퍼의 처음 위치부터 저장하도록 초기화
                        buffer.clear();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("File Split 도중 예외 발생", e);
        }

        System.out.println("Total Read  Bytes: " + totalReadBytes);
        System.out.println("Total Write Bytes: " + totalWriteBytes);
    }


    private static void writeHeader(String header, long readBytes, FileChannel splittedFileChannel) throws IOException {
        if (readBytes > 0 && !StringUtils.isEmpty(header)) {
            byte[] headerBytes = (header + System.lineSeparator()).getBytes(StandardCharsets.UTF_8);
            splittedFileChannel.write(ByteBuffer.wrap(headerBytes));
        }
    }
}
