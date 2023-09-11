<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Pausenaufsicht hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-multi-select title="Aufsichtführende Lehrkraft" :items="listLehrer"
					:item-text="(i: LehrerListeEintrag)=>`${i.kuerzel} (${i.vorname} ${i.nachname})`"
					:item-filter="filter" removable autocomplete ref="refLehrer" v-model="aufsicht" />
				<svws-ui-multi-select title="Aufsichtsbereiche" :items="listAufsichtsbereiche"
					:item-text="(i: StundenplanAufsichtsbereich)=>i.beschreibung"
					:item-filter="filterAufsichtsbereiche" removable autocomplete ref="refAufsichtsbereiche" v-model="aufsichtsbereich" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="add"> Pausenaufsicht hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import type { LehrerListeEintrag, List, StundenplanAufsichtsbereich} from "@core";
	import type { StundenplanPausenzeit} from "@core";
	import { SvwsUiMultiSelect } from "@ui";
	import { ref } from "vue";

	const props = defineProps<{
		pausenzeit: StundenplanPausenzeit;
		listLehrer: List<LehrerListeEintrag>;
		listAufsichtsbereiche: List<StundenplanAufsichtsbereich>;
		addAufsichtUndBereich: (pausenzeit: StundenplanPausenzeit, aufsicht: LehrerListeEintrag, bereich?: StundenplanAufsichtsbereich) => Promise<void>;
	}>();

	const modal = ref();
	const aufsicht = ref<LehrerListeEintrag|undefined>();
	const aufsichtsbereich = ref<StundenplanAufsichtsbereich|undefined>();

	const filter = (items: LehrerListeEintrag[], search: string) => {
		return items.filter(i => (i.istSichtbar === true) && (i.kuerzel.includes(search.toLocaleLowerCase()) || i.nachname?.toLocaleLowerCase().includes(search.toLocaleLowerCase())));
	};

	const filterAufsichtsbereiche = (items: StundenplanAufsichtsbereich[], search: string) => {
		return items.filter(i => (i.beschreibung) && (i.beschreibung.includes(search.toLocaleLowerCase()) || i.kuerzel?.toLocaleLowerCase().includes(search.toLocaleLowerCase())));
	};

	const openModal = () => {
		modal.value.openModal();
	}

	async function add() {
		if (aufsicht.value === undefined)
			return;
		await props.addAufsichtUndBereich(props.pausenzeit, aufsicht.value, aufsichtsbereich.value);
		modal.value.closeModal();
	}
</script>
