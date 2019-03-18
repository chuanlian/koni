import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class ConsumerQueue {


    @Test
    public void consumerMsg() {
        try {
            //1、获取mq连接
            Connection connection = ConnUtils.getConnection();
            //3、开启连接
            connection.start();
            //4、使用连接对象创建会话（session）对象
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //5、使用会话对象创建目标对象，包含queue和topic（一对一和一对多）
            Queue queue = session.createQueue("test_queue_3");
            //6、使用会话对象创建生产者对象
            MessageConsumer consumer = session.createConsumer(queue);
            //7、向consumer对象中设置一个messageListener对象，用来接收消息
            consumer.setMessageListener(new MessageListener() {

                public void onMessage(Message message) {
                    // TODO Auto-generated method stub
                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        try {
                            System.out.println(textMessage.getText());
                        } catch (JMSException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            });
            //8、程序等待接收用户消息
            System.in.read();
            //9、关闭资源
            consumer.close();
            session.close();
            connection.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex);
        } finally {
            System.out.println("ok");

        }
    }
}
