package com.github.sSuite.sWarp;

public class WarpException extends Exception {

	/**
	 * Unused.
	 */
	private static final long serialVersionUID = 2131156304782378327L;

	public enum WarpExceptionReason {
		NO_SUCH_WARP, UNSAFE_NAME, WARP_EXISTS;
	}

	private WarpExceptionReason reason;

	public WarpException(WarpExceptionReason reason) {
		super();
		this.reason = reason;
	}

	public WarpException(WarpExceptionReason reason, String message) {
		super(message);
		this.reason = reason;
	}

	public WarpExceptionReason getWarpExceptionReason() {
		return reason;
	}

}
