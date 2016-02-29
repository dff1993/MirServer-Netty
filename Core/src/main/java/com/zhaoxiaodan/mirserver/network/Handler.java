package com.zhaoxiaodan.mirserver.network;

import com.zhaoxiaodan.mirserver.network.packets.Packet;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Handler {
	protected Logger logger = LogManager.getLogger(this.getClass().getName());
	protected Session session;

	protected void exce(ChannelHandlerContext ctx, Packet packet) throws Exception {
		session = Session.getSession(ctx);
		if(null == session){
			logger.debug("new session for {}",ctx);
			session = Session.create(ctx);
		}
		session.db.begin();
		try{
			onPacket(packet);
			session.db.commit();
		}catch (Exception e){
			try{
				session.db.rollback();
			}catch (Exception e1){
				logger.error(e1);
			}
			throw e;
		}
	}

	protected void onDisconnect(ChannelHandlerContext ctx) throws Exception{
		session = Session.getSession(ctx);
		if(null == session){
			logger.error("session already remove for {}",ctx);
			return;
		}

		session.remove();
		onDisconnect();
	}

	public void onPacket(Packet packet) throws Exception{
		logger.error("overwrite it !!");
	}
	public void onDisconnect() throws Exception{
		logger.error("overwrite it !!");
	}
}
