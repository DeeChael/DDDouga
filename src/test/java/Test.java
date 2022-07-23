import net.deechael.dddouga.utils.T80Utils;

public class Test {

    public static void main(String[] args) {
        T80Utils.getPage(1).forEach(item -> System.out.println("[" + item.getId() + "] " + item.getName() + " - "));
    }

}
