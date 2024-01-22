<template>
	<svws-ui-modal :show="show">
		<template #modalTitle>Lehramt hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-select title="Lehramt" v-model="lehramt" :items="LehrerLehramt.values()" :item-text="(i: LehrerLehramt) => i.daten.text" headless />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="add" :disabled="lehramt === undefined"> Lehramt hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref, type Ref } from "vue";
	import { DeveloperNotificationException, LehrerLehramt, LehrerLehramtEintrag } from "@core";

	const props = defineProps<{
		show: () => Ref<boolean>;
		idLehrer: number,
		addLehramt: (eintrag: LehrerLehramtEintrag) => Promise<void>;
	}>();

	const lehramt = ref<LehrerLehramt | undefined>(undefined);

	function add() {
		if (lehramt.value === undefined)
			throw new DeveloperNotificationException("Die add-Methode darf nur aufgerufen werden, wenn ein gültiger Wert ausgewählt wurde.");
		const l = new LehrerLehramtEintrag();
		l.id = props.idLehrer;
		l.idLehramt = lehramt.value!.daten.id;
		l.idAnerkennungsgrund = null;
		void props.addLehramt(l);
		lehramt.value = undefined;
		props.show().value = false;
	}

</script>
