package com.sparta.taskflow.common.dataloader;

import java.time.LocalDateTime;

import com.github.javafaker.Faker;

public class FakerExample {

	private static final Faker faker = new Faker();

	// User data
	public static String generateUsername() {
		return faker.name().username();
	}

	public static String generatePassword() {
		return faker.internet().password();
	}

	public static String generateEmail() {
		return faker.internet().emailAddress();
	}

	public static String generateNickname() {
		return faker.name().firstName();
	}

	public static String generateIntroduction() {
		return faker.lorem().sentence();
	}

	// Board data
	public static String generateBoardName() {
		return faker.company().name();
	}

	public static String generateBoardDescription() {
		return faker.lorem().sentence();
	}

	// Section data
	public static String generateSectionContents() {
		return faker.lorem().characters(255);
	}

	// Card data
	public static String generateCardTitle() {
		return faker.lorem().sentence();
	}

	public static String generateCardContents() {
		return faker.lorem().characters(255);
	}

	public static LocalDateTime generateDueDate() {
		return LocalDateTime.now().plusDays(faker.number().numberBetween(1, 30));
	}

	// Comment data
	public static String generateCommentContents() {
		return faker.lorem().characters(255);
	}
}
