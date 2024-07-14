package com.sparta.taskflow.common.dataloader;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import com.github.javafaker.Faker;

public class DataGenerator {

	private static final Faker faker = new Faker();
	private static final Random random = new Random();

	// User data
	public static List<String> generateUniqueUsernames(int count) {
		return IntStream.range(0, count * 2)
			.mapToObj(i -> faker.regexify("[a-zA-Z0-9]{8,15}"))
			.distinct()
			.limit(count)
			.toList();
	}

	public static List<String> generateUniqueEmails(int count) {
		return IntStream.range(0, count * 2)
			.mapToObj(i -> faker.internet().emailAddress())
			.distinct()
			.limit(count)
			.toList();
	}

	public static List<String> generateUniqueNicknames(int count) {
		return IntStream.range(0, count * 2)
			.mapToObj(i -> faker.name().username())
			.distinct()
			.limit(count)
			.toList();
	}

	public static String generatePassword() {
		String upperCaseLetters = faker.regexify("[A-Z]");
		String lowerCaseLetters = faker.regexify("[a-z]");
		String digits = faker.regexify("[0-9]");
		String specialCharacters = faker.regexify("[#?!@$%^&*-]");
		String otherCharacters = faker.regexify("[A-Za-z0-9#?!@$%^&*-]{4,11}");

		return upperCaseLetters + lowerCaseLetters + digits + specialCharacters + otherCharacters;
	}

	public static String generateIntroduction() {
		return faker.lorem().sentence();
	}

	// Board data
	public static String generateBoardName() {
		return faker.company().name();
	}

	public static String generateBoardDescription() {
		return generateContents();
	}

	// Section data
	public static List<String> generateUniqueSectionContents(int count) {
		return IntStream.range(0, count * 2)
			.mapToObj(i -> generateContents())
			.distinct()
			.limit(count)
			.toList();
	}

	// Card data
	public static List<String> generateUniqueCardTitles(int count) {
		return IntStream.range(0, count * 2)
			.mapToObj(i -> generateContents())
			.distinct()
			.limit(count)
			.toList();
	}

	public static String generateCardContents() {
		return generateContents();
	}

	public static LocalDateTime generateDueDate() {
		return LocalDateTime.now().plusDays(faker.number().numberBetween(1, 30));
	}

	// Comment data
	public static String generateCommentContents() {
		return generateContents();
	}

	private static String generateContents() {
		StringBuilder content = new StringBuilder();
		while (content.length() < 255) {
			String sentence = faker.lorem().sentence();
			if (content.length() + sentence.length() > 255) {
				break;
			}
			content.append(sentence).append(" ");
		}
		return content.toString().trim();
	}
}
