<template>
	<svws-ui-modal :show="show">
		<template #modalTitle>Lehrbefähigung hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-select title="Lehrbefähigung" v-model="lehrbefaehigung" :items="LehrerLehrbefaehigung.values()" :item-text="(i: LehrerLehrbefaehigung) => i.daten.text" headless />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="add" :disabled="lehrbefaehigung === undefined"> Lehrbefähigung hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref, type Ref } from "vue";
	import { DeveloperNotificationException, LehrerLehrbefaehigung, LehrerLehrbefaehigungEintrag } from "@core";

	const props = defineProps<{
		show: () => Ref<boolean>;
		idLehrer: number,
		addLehrbefaehigung: (eintrag: LehrerLehrbefaehigungEintrag) => Promise<void>;
	}>();

	const lehrbefaehigung = ref<LehrerLehrbefaehigung | undefined>(undefined);

	function add() {
		if (lehrbefaehigung.value === undefined)
			throw new DeveloperNotificationException("Die add-Methode darf nur aufgerufen werden, wenn ein gültiger Wert ausgewählt wurde.");
		const l = new LehrerLehrbefaehigungEintrag();
		l.id = props.idLehrer;
		l.idLehrbefaehigung = lehrbefaehigung.value!.daten.id;
		l.idAnerkennungsgrund = null;
		void props.addLehrbefaehigung(l);
		lehrbefaehigung.value = undefined;
		props.show().value = false;
	}

</script>
