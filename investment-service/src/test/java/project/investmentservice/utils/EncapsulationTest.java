package project.investmentservice.utils;


import org.junit.Test;

public class EncapsulationTest {
    
    @Test
    public void 캡슐화테스트() {
        // 자식 클래스 객체가 부모 클래스로 캐스팅 되었을 때
        // 그대로 자식 클래스를 가지고 있는지 확인
        Children children = new Children("부모", "아이");
        Parent temp = children;

        System.out.println("temp.getClass() = " + temp.getClass());
    }
}

class Parent {
    private String msg;

    public Parent(String msg) {
        this.msg = msg;
    }
}

class Children extends Parent {
    private String name;

    public Children(String msg, String name) {
        super(msg);
        this.name = name;
    }
}
