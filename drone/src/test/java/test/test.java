package test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class test {
    public static void main(String[] args) {

        List<String> filterList=new ArrayList<>();
        Set<String> filterSet=new LinkedHashSet<>();
        filterList.add("a");
        filterList.add("b");
        filterList.add("c");
        List<String> abc;
        abc=filterList
                .stream()
                .filter(r-> !filterSet.add("f"))
                .collect(Collectors.toList());


        abc.forEach(r->{
            System.out.println(r);
        });

    }
}
