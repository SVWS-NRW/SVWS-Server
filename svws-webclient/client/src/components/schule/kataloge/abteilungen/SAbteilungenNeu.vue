<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Bezeichnung" :min-len="1" :max-len="50" v-model="data.bezeichnung" required :valid="fieldIsValid('bezeichnung')" />
				<svws-ui-text-input placeholder="Raum" :max-len="20" v-model="data.raum" :valid="fieldIsValid('raum')" />
				<svws-ui-select title="Lehrer" :items="manager().getLehrer().values()" :item-text="v => v.vorname + ' ' + v.nachname"
					:model-value="manager().getLehrer().get(data.idAbteilungsleiter)"
					@update:model-value="v => data.idAbteilungsleiter = v?.id ?? null" />
				<svws-ui-text-input placeholder="Email" type="email" :max-len="100" v-model="data.email" :valid="fieldIsValid('email')" />
				<svws-ui-text-input placeholder="Durchwahl" type="tel" :max-len="20" v-model="data.durchwahl" :valid="fieldIsValid('durchwahl')" />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addAbteilung" :disabled="!formIsValid">Speichern</svws-ui-button>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { AbteilungenNeuProps } from "~/components/schule/kataloge/abteilungen/SAbteilungenNeuProps";
	import { Abteilung, JavaString } from "@core";
	import { computed, ref } from "vue";

	const props = defineProps<AbteilungenNeuProps>();
	const data = ref<Abteilung>(new Abteilung());
	const isLoading = ref<boolean>(false);

	function fieldIsValid(field: keyof Abteilung | null) : (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'bezeichnung':
					return bezeichnungIsValid();
				case 'raum':
					return optionalInputIsValid(data.value.raum, 20);
				case 'email':
					return optionalInputIsValid(data.value.email, 100);
				case 'durchwahl':
					return optionalInputIsValid(data.value.durchwahl, 20);
				default:
					return true;
			}
		}
	}

	function bezeichnungIsValid() {
		return (!JavaString.isBlank(data.value.bezeichnung)) && (data.value.bezeichnung.length <= 50);
	}

	function optionalInputIsValid(input : string | null, maxLength : number) {
		if ((input === null) || (JavaString.isBlank(input)))
			return true;
		return input.length <= maxLength;
	}

	const formIsValid = computed(() => {
		// alle Felder auf validity prüfen
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof Abteilung);
			const fieldValue = data.value[field as keyof Abteilung] as string | null;
			return validateField(fieldValue);
		})
	})

	async function addAbteilung() {
		if (isLoading.value)
			return;

		props.checkpoint.active = false;
		isLoading.value = true;
		data.value.idSchuljahresabschnitt = props.manager().getSchuljahresabschnittSchule().id;
		const { id, klassenzuordnungen, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

	function cancel() {
		props.checkpoint.active = false;
		void props.goToDefaultView(null);
	}

</script>
