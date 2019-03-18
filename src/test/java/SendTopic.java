import org.junit.Test;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

public class SendTopic {

    @Test
    public void TestTopicProducer() throws Exception {
        try {
            int flag = 0;
            //1、获取一个链接
            Connection connection = ConnUtils.getConnection();
            //3、开启连接
            connection.start();
            while (true) {
                if (flag > 5000) {
                    break;
                }
                //4、使用连接对象创建会话（session）对象
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                //5、使用会话对象创建目标对象，包含queue和topic（一对一和一对多）
                Topic topic = session.createTopic(ConnUtils.topic_name);
                //6、使用会话对象创建生产者对象
                MessageProducer producer = session.createProducer(topic);

                //7、使用会话对象创建一个消息对象
                TextMessage textMessage = session.createTextMessage("hello!test-topic:" + flag);
                //8、发送消息
                producer.send(textMessage);
                //9、关闭资源
                producer.close();
                session.close();
                flag++;
            }
            connection.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            System.out.println("ok");
        }
    }
}
