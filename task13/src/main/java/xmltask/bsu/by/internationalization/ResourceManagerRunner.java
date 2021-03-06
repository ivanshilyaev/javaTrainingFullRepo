package xmltask.bsu.by.internationalization;

import xmltask.bsu.by.internationalization.ResourceManager;

import java.util.Locale;

public class ResourceManagerRunner {
    public static void main(String[] args) {
        ResourceManager manager = ResourceManager.INSTANCE;
        System.out.println(manager.getString("str1"));
        manager.changeResource(new Locale("by", "BY"));
        System.out.println(manager.getString("str1"));
    }
}
