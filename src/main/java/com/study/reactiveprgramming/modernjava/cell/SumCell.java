package com.study.reactiveprgramming.modernjava.cell;

public class SumCell extends SimpleCell{
    int left;
    int right;

    public SumCell(String name) {
        super(name);
    }

    public void setLeft(int left) {
        this.left = left;
        sum();
    }

    private void sum() {
        onNext(left+right);
    }

    public void setRight(int right) {
        this.right = right;
        sum();
    }
}
