<template>
	<svws-ui-modal :show="show" @update:show="value => emit('update:show', value)">
		<template #modalTitle>Lehrbefähigung hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-select title="Lehrbefähigung" v-model="lehrbefaehigung" :items="LehrerLehrbefaehigung.values()" :item-text="i => i.daten(schuljahr)?.text ?? '—'" headless />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="emit('update:show', false)"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="add" :disabled="lehrbefaehigung === undefined"> Lehrbefähigung hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import { DeveloperNotificationException, LehrerLehrbefaehigung, LehrerLehrbefaehigungEintrag } from "@core";

	const props = defineProps<{
		show: boolean;
		idLehrer: number,
		addLehrbefaehigung: (eintrag: LehrerLehrbefaehigungEintrag) => Promise<void>;
		schuljahr: number;
	}>();

	const emit = defineEmits<{
		"update:show": [show: boolean];
	}>();

	const lehrbefaehigung = ref<LehrerLehrbefaehigung | undefined>(undefined);

	function add() {
		const daten = lehrbefaehigung.value?.daten(props.schuljahr) ?? null;
		if (daten === null)
			throw new DeveloperNotificationException("Die add-Methode darf nur aufgerufen werden, wenn ein gültiger Wert ausgewählt wurde.");
		const l = new LehrerLehrbefaehigungEintrag();
		l.id = props.idLehrer;
		l.idLehrbefaehigung = daten.id;
		l.idAnerkennungsgrund = null;
		void props.addLehrbefaehigung(l);
		lehrbefaehigung.value = undefined;
		emit('update:show', false);
	}

</script>
