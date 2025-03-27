<template>
	<svws-ui-modal :show="show" @update:show="value => emit('update:show', value)">
		<template #modalTitle>Fachrichtung hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-select title="Fachrichtung" v-model="fachrichtung" :items="LehrerFachrichtung.data().getWerteBySchuljahr(schuljahr)" :item-text="i => i.daten(schuljahr)?.text ?? '—'" headless />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="emit('update:show', false)"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="add" :disabled="fachrichtung === undefined"> Fachrichtung hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { shallowRef } from "vue";
	import { DeveloperNotificationException, LehrerFachrichtung, LehrerFachrichtungEintrag } from "@core";

	const props = defineProps<{
		show: boolean;
		idLehrer: number;
		schuljahr: number;
		addFachrichtung: (eintrag: LehrerFachrichtungEintrag) => Promise<void>;
	}>();

	const emit = defineEmits<{
		"update:show": [show: boolean];
	}>();

	const fachrichtung = shallowRef<LehrerFachrichtung | undefined>(undefined);

	async function add() {
		const daten = fachrichtung.value?.daten(props.schuljahr) ?? null;
		if (daten === null)
			throw new DeveloperNotificationException("Die add-Methode darf nur aufgerufen werden, wenn ein gültiger Wert ausgewählt wurde.");
		const l = new LehrerFachrichtungEintrag();
		l.id = props.idLehrer;
		l.idFachrichtung = daten.id;
		l.idAnerkennungsgrund = null;
		await props.addFachrichtung(l);
		fachrichtung.value = undefined;
		emit('update:show', false)
	}

</script>
