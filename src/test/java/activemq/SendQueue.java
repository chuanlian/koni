package activemq;

import org.junit.Test;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class SendQueue {


    @Test
    public void sendMsg() {
        long start = System.currentTimeMillis();
        try {
            int flag = 0;
            //1、获取一个链接
            Connection connection = ConnUtils.getConnectionNoPwd();
            //3、开启连接
            connection.start();
            while (true) {
                if (flag > 999) {
                    break;
                }
                //4、使用连接对象创建会话（session）对象
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                //5、使用会话对象创建目标对象，包含queue和topic（一对一和一对多）
                Queue queue = session.createQueue(ConnUtils.queue_name);
                //6、使用会话对象创建生产者对象
                MessageProducer producer = session.createProducer(queue);
                //7、使用会话对象创建一个消息对象
                TextMessage textMessage = session.createTextMessage("hello:" + System.currentTimeMillis());
                //8、发送消息
                producer.send(textMessage);
                //9、关闭资源
                producer.close();
                session.close();
                flag++;
            }
            //10、关闭资源
            connection.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex);
        } finally {
            long costTime = System.currentTimeMillis() - start;
            System.out.println("sendMsg cost time:" + costTime);
            System.out.println("ok");
        }
    }


}
