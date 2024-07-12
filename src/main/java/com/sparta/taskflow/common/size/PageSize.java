package com.sparta.taskflow.common.size;

public enum PageSize {
	BOARD(4),
	SECTION(4),
	CARD(5),
	COMMENT(10);

	private final int size;

	PageSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}
}