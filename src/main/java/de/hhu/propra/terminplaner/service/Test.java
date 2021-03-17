package de.hhu.propra.terminplaner.service;

import java.util.ArrayList;
import java.util.List;

public class Test {


  public static void main(String[] args) {
    List<String> l = new ArrayList<>();

    String a = "a";
    String b = "b";
    String c = "c";
    String d = "d";

    l.add(a);
    l.add(b);
    l.add(c);
    l.add(d);

    System.out.println(l.get(0));
    System.out.println(l.get(1));
    System.out.println(l.get(3));
    l.remove(a);
    System.out.println(l.get(0));

  }
}
