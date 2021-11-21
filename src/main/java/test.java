import java.awt.*;
import java.io.*;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

/**
 * Project name(项目名称)：猜数字小游戏
 * Package(包名): PACKAGE_NAME
 * Class(类名): test
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2021/11/21
 * Time(创建时间)： 20:10
 * Version(版本): 1.0
 * Description(描述)：
 * 示例中用到 Properties 类的几个方法，方法说明如下：
 * getProperty (String key)：用指定的键在此属性列表中搜索属性。也就是通过参数 key，得到 key 所对应的 value。
 * load (InputStream inStream)：从输入流中读取属性列表（键和元素对）。通过对指定的文件进行装载来获取该文件中的所有键值对。
 * 以供 getProperty (String key) 来搜索。
 * setProperty (String key, String value) ：调用 Hashtable 的方法 put，通过调用基类的 put 方法来设置键值对。
 * store (OutputStream out, String comments)：与 load 方法相反，该方法是将键值对写入到指定的文件中。
 */

public class test
{
    public static int getIntRandom(int min, int max)         //空间复杂度和时间复杂度更低
    {
        if (min > max)
        {
            min = max;
        }
        return min + (int) (Math.random() * (max - min + 1));
    }

    public static int getIntRandom1(int min, int max)          //获取int型的随机数
    {
        if (min > max)
        {
            min = max;
        }
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }


    /**
     * 负责调用对应的方法，实现整个案例的逻辑关系
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        File file = new File("count.txt");
        if (!file.exists())
        {
            System.out.println("欢迎使用！！！");
            FileWriter fileWriter1 = null;
            FileWriter fileWriter2 = null;
            try
            {
                fileWriter1 = new FileWriter(file);
                fileWriter2 = new FileWriter("way.txt");
                fileWriter1.write("count=0");
                fileWriter2.write("way=0");
            }
            catch (Exception e)
            {
                Toolkit.getDefaultToolkit().beep();
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    if (fileWriter1 != null)
                    {
                        fileWriter1.close();
                    }
                    if (fileWriter2 != null)
                    {
                        fileWriter2.close();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        while (true)
        {
            // 获取游戏次数
            int count = getCount();
            // 获取付费状态
            boolean flag = getCondition();
            // 如果已付费，提示用户游戏次数解封可以继续游戏
            if (flag)
            {
                System.out.println("游戏已经付费，游戏次数已解封！");
                game();
            }
            else
            {
                // 未付费且游戏次数超过5次时，提示试玩结束，要付费
                if (count >= 5)
                {
                    System.out.println("试玩已经结束，请付费！");
                    getMoney();
                }
                else
                {
                    // 未付费且游戏次数未超过5次时，继续游戏，游戏次数加1
                    System.out.println("----" + "试玩第" + (count + 1) + "次" + "----");
                    game();
                    writeCount();
                }
            }
        }
    }

    /**
     * 获取已经玩过的次数
     *
     * @return temp count.txt文件中的游戏次数
     * @throws IOException
     */
    private static int getCount() throws IOException
    {
        // 创建Properties对象
        Properties prop = new Properties();
        // 使用FileReader对象获取count文件中的游戏次数
        prop.load(new FileReader("count.txt"));
        String property = prop.getProperty("count");
        int temp = Integer.parseInt(property);
        return temp;
    }

    /**
     * 支付方法，支付成功则把支付状态改为“1”并存到数据库，之后可以无限次玩游戏
     *
     * @throws IOException
     */
    private static void getMoney() throws IOException
    {
        System.out.println("请支付5元！");
        // 获取键盘录入数据
        Scanner input = new Scanner(System.in);
        int nextInt;
        //控制台输入变量:nextInt
        int errCount = 0;
        while (true)
        {
            try
            {
                //min:0
                //max:1000
                System.out.print("请输入金额：");
                nextInt = input.nextInt();
                if (nextInt >= 0 && nextInt <= 1000)
                {
                    break;
                }
                else
                {
                    errCount++;
                    Toolkit.getDefaultToolkit().beep();
                    if (errCount > 10)
                    {
                        System.err.println("错误次数过多！！！退出");
                        System.exit(1);
                    }
                    System.out.println("输入的数据不在范围内! 范围：[0,1000]");
                }
            }
            catch (Exception e)
            {
                errCount++;
                if (errCount > 5)
                {
                    Toolkit.getDefaultToolkit().beep();
                    System.err.println("错误次数过多！！！退出");
                    System.exit(1);
                }
                else
                {
                    Toolkit.getDefaultToolkit().beep();
                    System.out.println("输入错误！！！请重新输入！");
                    input.nextLine();
                }
            }
        }
        if (nextInt == 5)
        {
            // 创建Properties对象
            Properties prop = new Properties();
            prop.setProperty("way", "1");
            // 使用FileWriter类将支付状态写入到way文件
            prop.store(new FileWriter("way.txt"), null);
        }
    }

    /**
     * 将试玩的次数写入文档并保存
     *
     * @throws IOException
     */
    private static void writeCount() throws IOException
    {
        // 创建Properties对象
        Properties prop = new Properties();
        int count = getCount();
        // 写入文件
        prop.setProperty("count", (count + 1) + "");
        prop.store(new FileWriter("count.txt"), null);
    }

    /**
     * 用来获取每次启动时的付费状态
     *
     * @return flag 是否付费
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static boolean getCondition() throws FileNotFoundException, IOException
    {
        boolean flag = false;
        // 创建Properties对象
        Properties prop = new Properties();
        // 读取way.txt文件，获取支付状态
        prop.load(new FileReader("way.txt"));
        String property = prop.getProperty("way");
        int parseInt = Integer.parseInt(property);
        // way的值等于1时，为已付费
        if (parseInt == 1)
        {
            flag = true;
        }
        else
        {
            flag = false;
        }
        return flag;
    }

    /**
     * 实现游戏产生数字，获取玩家所猜数字等， 并对玩家每次输入，都会有相应的提示
     */
    private static void game()
    {
        // 产生随机数1~10
        int random = getIntRandom(0, 10);
        // 获取键盘录入数据
        Scanner input = new Scanner(System.in);
        System.out.println("欢迎来到猜数字小游戏！");
        // while循环进行游戏
        while (true)
        {
            int guess;
            //控制台输入变量:guess
            int errCount = 0;
            while (true)
            {
                try
                {
                    //min:0
                    //max:10
                    System.out.println("请输入你猜的数据:");
                    guess = input.nextInt();
                    if (guess >= 0 && guess <= 10)
                    {
                        break;
                    }
                    else
                    {
                        errCount++;
                        Toolkit.getDefaultToolkit().beep();
                        if (errCount > 10)
                        {
                            System.err.println("错误次数过多！！！退出");
                            System.exit(1);
                        }
                        System.out.println("输入的数据不在范围内! 范围：[0,10]");
                    }
                }
                catch (Exception e)
                {
                    errCount++;
                    if (errCount > 5)
                    {
                        Toolkit.getDefaultToolkit().beep();
                        System.err.println("错误次数过多！！！退出");
                        System.exit(1);
                    }
                    else
                    {
                        Toolkit.getDefaultToolkit().beep();
                        System.out.println("输入错误！！！请重新输入！");
                        input.nextLine();
                    }
                }
            }
            if (guess > random)
            {
                System.out.println("大了");
            }
            else if (guess < random)
            {
                System.out.println("小了");
            }
            else
            {
                System.out.println("猜对了哦！");
                break;
            }
        }
    }
}
