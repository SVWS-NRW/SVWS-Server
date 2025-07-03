<template>
	<svws-ui-modal :show="show" @update:show="value => emit('update:show', value)">
		<template #modalTitle>Lehramt hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-select title="Lehramt" v-model="lehramt" :items="LehrerLehramt.data().getWerteBySchuljahr(schuljahr)"
					:item-text="i => i.daten(schuljahr)?.text ?? '—'" statistics />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="emit('update:show', false)"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="add" :disabled="lehramt === undefined"> Lehramt hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { shallowRef } from "vue";
	import { DeveloperNotificationException, LehrerLehramt, LehrerLehramtEintrag } from "@core";

	const props = defineProps<{
		show: boolean;
		idLehrer: number;
		addLehramt: (eintrag: LehrerLehramtEintrag) => Promise<void>;
		schuljahr: number;
	}>();

	const emit = defineEmits<{
		"update:show": [show: boolean];
	}>();

	const lehramt = shallowRef<LehrerLehramt | undefined>(undefined);

	async function add() {
		const daten = lehramt.value?.daten(props.schuljahr) ?? null;
		if (daten === null)
			throw new DeveloperNotificationException("Die add-Methode darf nur aufgerufen werden, wenn ein gültiger Wert ausgewählt wurde.");
		const l = new LehrerLehramtEintrag();
		l.id = props.idLehrer;
		l.idLehramt = daten.id;
		l.idAnerkennungsgrund = null;
		await props.addLehramt(l);
		lehramt.value = undefined;
		emit('update:show', false);
	}

</script>
