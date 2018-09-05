package com.github.ssuite.swarp.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RequestRejectEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Player sourcePlayer;
	private String token;
	
	public RequestRejectEvent(Player source, String token) {
		sourcePlayer = source;
		this.token = token;
	}
	
	public Player getSourcePlayer() {
		return sourcePlayer;
	}
	
	public String getToken() {
		return token;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
