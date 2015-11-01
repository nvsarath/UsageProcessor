package com.verizon.usage;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import com.verizon.usage.clent.HazelCastClientUtil;

public class DBPersister {

   	Cluster cluster;
	Session session;
	
    public void createSession(){
    	cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
    	
    	session = cluster.connect("usage");
    	/*String query = "CREATE KEYSPACE datausage WITH replication "
    			   + "= {'class':'SimpleStrategy', 'replication_factor':1}; ";
    			session.execute(query);*/
    	session.execute("USE usage");
    	System.out.println("session done");
    }
    
    public void populateData(String p_custid,String p_usage,String p_date){
 /*   	String instQrystr = "INSERT INTO cust_usage (custid, usage_date, usage) " +
    			"VALUES ('1234', ,123 )";*/
    	PreparedStatement statement = session.prepare(
    		      "INSERT INTO cust_usage (custid, usage_date, usage) " +
    		      "VALUES (?, ?, ?);");
    	BoundStatement boundStatement = new BoundStatement(statement);
    	boundStatement.setString("custid", p_custid);
    	boundStatement.setString("usage_date", p_date);
    	boundStatement.setString("usage",p_usage);
    	
    	
    	session.execute(boundStatement);
    	System.out.println("insert done");
    }
    
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBPersister obj = new DBPersister ();
		obj.createSession();
	//	HazelCastClientUtil util = new HazelCastClientUtil();
		HazelcastInstance client = HazelCastClientUtil.getClient();
		IMap<Object, Object> imap = client.getMap("customers");
		Iterator custItr = imap.keySet().iterator();
		
		while(custItr.hasNext()) {
			String customrId = String.valueOf(custItr.next());
			Map<String, String> custObj= (Map)imap.get(customrId);
			System.out.println("after getting cust obj");
			if (custObj != null && !custObj.isEmpty()){
				Iterator<Map.Entry<String, String>> it = custObj.entrySet().iterator();
				while ( it.hasNext()) {
					Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
				    String key = entry.getKey();
				    String value = entry.getValue();
				    System.out.println("key "+key +" value "+value);
				    obj.populateData(customrId,value,key);
				}
			}else{
				System.out.println("cust not found");
			}			
		}
		System.exit(0);
	}

	
	public static void loadingDataIntoCasandra() {
		// TODO Auto-generated method stub
		DBPersister obj = new DBPersister ();
		obj.createSession();
	//	HazelCastClientUtil util = new HazelCastClientUtil();
		HazelcastInstance client = HazelCastClientUtil.getClient();
		IMap<Object, Object> imap = client.getMap("customers");
		Iterator custItr = imap.keySet().iterator();
		
		while(custItr.hasNext()) {
			String customrId = String.valueOf(custItr.next());
			Map<String, String> custObj= (Map)imap.get(customrId);
			System.out.println("after getting cust obj");
			if (custObj != null && !custObj.isEmpty()){
				Iterator<Map.Entry<String, String>> it = custObj.entrySet().iterator();
				while ( it.hasNext()) {
					Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
				    String key = entry.getKey();
				    String value = entry.getValue();
				    System.out.println("key "+key +" value "+value);
				    obj.populateData(customrId,value,key);
				}
			}else{
				System.out.println("cust not found");
			}			
		}
	
	}

	
}
