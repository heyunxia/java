package com.heyunxia;

/**
 * Hello world!
 */
public class App {
    private static int finalCount = 0;
    private int lastBottle = 0; //剩余瓶子
    private int lastLid = 0;    //剩余盖子

    public static void main(String[] args) {
        App app = new App();
        app.letDown(10);// 初始10瓶
    }

    /**
     * @param total 　总瓶子数
     */
    public void letDown(int total) {
        finalCount = finalCount + total;
        int bottle = total; //瓶子
        int lid = bottle;   //盖子
        bottle = lastBottle + bottle;
        lid = lastLid + lid;

        int sum = bottle / 2 + lid / 4;
        if (sum != 0) {

            lastBottle = bottle % 2;
            lastLid = lid % 4;

            letDown(sum);
        } else if (sum == 0) {
            System.out.println("已喝：" + finalCount + " 瓶");
            System.out.println("剩余瓶子：" + bottle + "个，　盖子：" + lid + "个");
        }
    }
}

/*@NodeEntity
class User{
    @GraphId long id;

    @Indexed
    private String loginName;
}*/
