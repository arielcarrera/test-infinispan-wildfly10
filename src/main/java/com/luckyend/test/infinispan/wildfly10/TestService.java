package com.luckyend.test.infinispan.wildfly10;

import java.text.DateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.infinispan.AdvancedCache;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;

@Stateless
@LocalBean
public class TestService {

	@Resource(lookup = "java:jboss/infinispan/container/TEST")
	private EmbeddedCacheManager manager;
	
	private DateFormat df = DateFormat.getTimeInstance();

	public String testConfig() {
		StringBuffer sb = new StringBuffer();
		try {
			
			//manager.defineConfiguration("default", conf());
			
			sb.append("cache manager class: " + getClassName(manager));

			sb.append("\ncache names at start: " + manager.getCacheNames());
			
			//default (x)
			sb.append("\n\n** default cache**:");
			sb.append("\n* expected configuration (name=x,expiration(lifespan=9000,max-idle=3000),eviction(strategy=LRU,max-entries=2) *");
			getConfigCache(sb, null);

			//x
			sb.append("\n\n** x cache**:");
			sb.append("\n* expected configuration (name=x,expiration(lifespan=9000,max-idle=3000),eviction(strategy=LRU,max-entries=2) *");
			getConfigCache(sb, "x");
			
			//y
			sb.append("\n\n** y cache**:");
			sb.append("\n* expected configuration (name=y,expiration(lifespan=3000,max-idle=1000),eviction(strategy=LRU,max-entries=1) *");
			getConfigCache(sb, "y");
			
			sb.append("\n\n\ncache names at the end: " + manager.getCacheNames());
			
			return sb.toString();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void getConfigCache(StringBuffer sb, String name) {
		Cache<Object, Object> cache;
		if (name == null) cache = manager.getCache();
		else cache = manager.getCache(name);
		
		sb.append("\ncache class: " + getClassName(cache));

		if (cache != null) {
			sb.append("\nname: " + cache.getName());
			sb.append("\neviction: " + cache.getCacheConfiguration().eviction().toString());
			sb.append("\nexpiration: "+ cache.getCacheConfiguration().expiration().toString());
			AdvancedCache<Object, Object> advancedDefaultCache = cache.getAdvancedCache();
			sb.append("\ntx manager: " + advancedDefaultCache.getTransactionManager());
		}
	}
	
	private Cache<Object, Object> getCache(String name){
//		manager.defineConfiguration("default", conf());
		return manager.getCache(name);
	}
	
//	private Configuration conf() {
//	Transport transport = manager.getGlobalComponentRegistry().getGlobalConfiguration().transport().transport();
//	
//	return new ConfigurationBuilder()
//		.clustering()
//			.cacheMode(transport == null? LOCAL : REPL_SYNC)
//			
//		.transaction()
//			.transactionMode(TRANSACTIONAL)
//			.transactionManagerLookup(new GenericTransactionManagerLookup())
//			.autoCommit(false)
//		
////		.lockingMode(LockingMode.PESSIMISTIC)
////			.locking()
////			.isolationLevel(SERIALIZABLE)
////			
//		.persistence()
//			.passivation(false)
//			.addSingleFileStore()
//			.purgeOnStartup(true)
//	.build();
//}
	
	public String put() {
		String time = df.format(new Date());
		String result = "put (current time:" +time +  "):\n";
		try {
			Cache<Object, Object> cacheX = getCache("x");
			cacheX.put("1", "TEST:" + time);
			
			Cache<Object, Object> cacheY = getCache("y");
			cacheY.put("1", "TEST:" + time);
			
			return result + "\nOK";
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public String read() {
		String time = df.format(new Date());
		String result = "read (current time:" +time +  "):\n";
		try {
			
			Cache<Object, Object> cacheX = getCache("x");
			Object x = cacheX.get("1");
			if (x != null) 
				result += "x:" + x;
			else
				result += "x: not found";
				
			
			Cache<Object, Object> cacheY = getCache("y");
			Object y = cacheY.get("1");
			if (y != null) 
				result += "\ny:" + y;
			else
				result += "\ny: not found";
			
			return result;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	
	private String getClassName(Object obj) {
		return obj != null? obj.getClass().toString() : "-";
	}

}
