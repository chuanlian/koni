import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
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
            Connection connection = getConnection();
            //3、开启连接
            connection.start();
            while (true) {
                if (flag > 5000) {
                    break;
                }
                //4、使用连接对象创建会话（session）对象
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                //5、使用会话对象创建目标对象，包含queue和topic（一对一和一对多）
                Queue queue = session.createQueue("test-queue");
                //6、使用会话对象创建生产者对象
                MessageProducer producer = session.createProducer(queue);
                //7、使用会话对象创建一个消息对象
                TextMessage textMessage = session.createTextMessage("hello!test-queue" + flag);
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

    private Connection getConnection() throws JMSException {
        String userName = "system";
        String pwd = "manager";
        String brokerUrl = "tcp://127.0.0.1:61616";
        //1、创建工厂连接对象，需要制定ip和端口号
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName, pwd, brokerUrl);
        //2、使用连接工厂创建一个连接对象
        Connection connection = connectionFactory.createConnection();
        return connection;
    }
}
