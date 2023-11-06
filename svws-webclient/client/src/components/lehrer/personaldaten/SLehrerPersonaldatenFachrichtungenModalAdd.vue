<template>
	<svws-ui-modal :show="show">
		<template #modalTitle>Fachrichtung hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-select title="Fachrichtung" v-model="fachrichtung" :items="LehrerFachrichtung.values()" :item-text="(i: LehrerFachrichtung) => i.daten.text" headless />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="add" :disabled="fachrichtung == undefined"> Fachrichtung hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref, type Ref } from "vue";
	import { DeveloperNotificationException, LehrerFachrichtung, LehrerFachrichtungEintrag } from "@core";

	const props = defineProps<{
		show: () => Ref<boolean>;
		idLehrer: number,
		addFachrichtung: (eintrag: LehrerFachrichtungEintrag) => Promise<void>;
	}>();

	const fachrichtung = ref<LehrerFachrichtung | undefined>(undefined);

	function add() {
		if (fachrichtung.value == undefined)
			throw new DeveloperNotificationException("Die add-Methode darf nur aufgerufen werden, wenn ein gültiger Wert ausgewählt wurde.");
		const l = new LehrerFachrichtungEintrag();
		l.id = props.idLehrer;
		l.idFachrichtung = fachrichtung.value!.daten.id;
		l.idAnerkennungsgrund = null;
		void props.addFachrichtung(l);
		fachrichtung.value = undefined;
		props.show().value = false;
	}

</script>
