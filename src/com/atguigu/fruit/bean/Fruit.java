package com.atguigu.fruit.bean;

import java.io.Serializable;
import java.util.Objects;

public class Fruit implements Serializable {

  //实现serializable接口。
  private static final long serialVersionUID = 1234567L;

  private int fid;
  private String fname;
  private int price;
  private int fcount;
  private String remark;

  public Fruit() {
  }

  public Fruit(Integer fid, String fname, String remark) {
    this.fid = fid;
    this.fname = fname;
    this.remark = remark;
  }

  public Fruit(String fname, Integer price, Integer fcount, String remark) {
    this.fname = fname;
    this.price = price;
    this.fcount = fcount;
    this.remark = remark;
  }

  public Fruit(Integer fid, String fname, Integer price, Integer fcount, String remark) {
    this.fid = fid;
    this.fname = fname;
    this.price = price;
    this.fcount = fcount;
    this.remark = remark;
  }

  public Integer getFid() {
    return fid;
  }

  public void setFid(Integer fid) {
    this.fid = fid;
  }

  public String getFname() {
    return fname;
  }

  public void setFname(String fname) {
    this.fname = fname;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Integer getFcount() {
    return fcount;
  }

  public void setFcount(Integer fcount) {
    this.fcount = fcount;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Override
  public String toString() {
    return "Fruit{" +
        "fid=" + fid +
        ", fname='" + fname + '\'' +
        ", price=" + price +
        ", fcount=" + fcount +
        ", remark='" + remark + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Fruit fruit = (Fruit) o;
    return fid == fruit.fid && price == fruit.price && fcount == fruit.fcount
        && Objects.equals(fname, fruit.fname) && Objects.equals(remark,
        fruit.remark);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fid);
  }
}
