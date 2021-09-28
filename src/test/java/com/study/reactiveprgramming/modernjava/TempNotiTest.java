package com.study.reactiveprgramming.modernjava;

import io.reactivex.Observable;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TempNotiTest {
    Logger logger= LoggerFactory.getLogger(TempNotiTest.class);
    @Test
    void 온도_발행(){
        Observable<TempInfo> observable=getObservableTemp("town1","town2","town3");
        observable.blockingSubscribe(System.out::println);
    }

    private Observable<TempInfo> getObservableTemp(String ... towns) {
        return Observable.merge(Arrays.stream(towns)
                .map(this::getTemp)
                .collect(Collectors.toList()));
    }

    private Observable<TempInfo> getTemp(String town) {
        return Observable.<TempInfo>create(observableEmitter ->
                Observable.interval(1, TimeUnit.SECONDS)
                        .subscribe(i ->{
                                    if(!observableEmitter.isDisposed()){
                                        if(i>4){
                                            observableEmitter.onComplete();
                                        }else{
                                            try{
                                                observableEmitter.onNext(TempInfo.fetch(town));
                                            }catch(Throwable e){
                                                observableEmitter.onError(e);
                                            }

                                        }
                                    }
                                }
                        )).map( temp -> new TempInfo(temp.getTown(), (temp.getTemp()-32)*5/9));
    }

    static class TempInfo{
        public static final Random random=new Random();

        private final String town;
        private final int temp;

        public  TempInfo(String town, int temp){
            this.town = town;
            this.temp = temp;
        }

        public static TempInfo fetch(String town){
            if(random.nextInt(10)==0)
                throw new RuntimeException("error!");

            return new TempInfo(town, random.nextInt(100));
        }

        @Override
        public String toString() {
            return "TempInfo{" +
                    "town='" + town + '\'' +
                    ", temp=" + temp +
                    '}';
        }

        public String getTown() {
            return town;
        }

        public int getTemp() {
            return temp;
        }
    }
}
