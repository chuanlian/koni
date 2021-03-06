package activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

public class ConnUtils {

    public static final String queue_name = "test_queue_5";

    public static final String topic_name = "test_topic_1";

    public static final String BROKER = "failover:(tcp://10.94.167.81:8070,tcp://10.94.162.58:8070)";

    /**
     * @return
     * @throws JMSException
     */
    public static Connection getConnection() throws JMSException {
        String userName = "system";
        String pwd = "manager";
        //1、创建工厂连接对象，需要制定ip和端口号
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName, pwd, BROKER);
        //2、使用连接工厂创建一个连接对象
        Connection connection = connectionFactory.createConnection();
        return connection;
    }

    /**
     * @return
     * @throws JMSException
     */
    public static Connection getConnectionNoPwd() throws JMSException {
        //1、创建工厂连接对象，需要制定ip和端口号
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER);
        //2、使用连接工厂创建一个连接对象
        Connection connection = connectionFactory.createConnection();
        return connection;
    }
}
