package de.svws_nrw.service.filter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AttributeTarget")
class AttributeTargetTest {

	@Test
	@DisplayName("Konstruktor | übernimmt Entity-Name und Entity-Property")
	void constructorKeepsValues() {
		final AttributeTarget target = new AttributeTarget("de.svws_nrw.Entity", "idStatus");

		assertThat(target.entityName()).isEqualTo("de.svws_nrw.Entity");
		assertThat(target.entityProperty()).isEqualTo("idStatus");
	}

	@Test
	@DisplayName("Konstruktor | wirft NullPointerException mit deutscher Meldung, wenn der Entity-Name null ist")
	void constructorRejectsNullEntityName() {
		assertThatThrownBy(() -> new AttributeTarget(null, "idStatus"))
				.isInstanceOf(NullPointerException.class)
				.hasMessage("Der Name der Entity darf nicht null sein.");
	}

	@Test
	@DisplayName("Konstruktor | wirft NullPointerException mit deutscher Meldung, wenn das Entity-Property null ist")
	void constructorRejectsNullEntityProperty() {
		assertThatThrownBy(() -> new AttributeTarget("de.svws_nrw.Entity", null))
				.isInstanceOf(NullPointerException.class)
				.hasMessage("Der Name des Entity Properties darf nicht null sein.");
	}

	@Test
	@DisplayName("Konstruktor | wirft bei zwei null-Werten die Meldung zum Entity-Namen, da dieser zuerst geprüft wird")
	void constructorChecksEntityNameFirst() {
		assertThatThrownBy(() -> new AttributeTarget(null, null))
				.isInstanceOf(NullPointerException.class)
				.hasMessage("Der Name der Entity darf nicht null sein.");
	}

	@Test
	@DisplayName("of | liefert ein AttributeTarget mit den übergebenen Werten")
	void ofCreatesTarget() {
		final AttributeTarget target = AttributeTarget.of("de.svws_nrw.Entity", "idStatus");

		assertThat(target).isEqualTo(new AttributeTarget("de.svws_nrw.Entity", "idStatus"));
	}

	@Test
	@DisplayName("of | wirft NullPointerException, wenn ein Parameter null ist")
	void ofRejectsNull() {
		assertThatThrownBy(() -> AttributeTarget.of(null, "idStatus")).isInstanceOf(NullPointerException.class);
		assertThatThrownBy(() -> AttributeTarget.of("de.svws_nrw.Entity", null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	@DisplayName("Gleichheit | Targets mit gleichen Werten sind gleich, bei abweichendem Entity-Namen oder Property nicht")
	void equalsByValues() {
		assertThat(AttributeTarget.of("Entity", "property"))
				.isEqualTo(AttributeTarget.of("Entity", "property"))
				.hasSameHashCodeAs(AttributeTarget.of("Entity", "property"))
				.isNotEqualTo(AttributeTarget.of("Andere", "property"))
				.isNotEqualTo(AttributeTarget.of("Entity", "anderes"));
	}
}
