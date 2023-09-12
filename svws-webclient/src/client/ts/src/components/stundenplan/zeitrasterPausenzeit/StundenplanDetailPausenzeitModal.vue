<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Pausenaufsicht hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-multi-select title="Aufsichtführende Lehrkraft" :items="listLehrer"
					:item-text="(i: LehrerListeEintrag)=>`${i.kuerzel} (${i.vorname} ${i.nachname})`"
					:item-filter="filter" removable autocomplete :model-value="undefined" ref="refLehrer" />
				<svws-ui-multi-select title="Aufsichtsbereiche" :items="listAufsichtsbereiche"
					:item-text="(i: StundenplanAufsichtsbereich)=>i.beschreibung" ref="refAufsichtsbereich"
					:item-filter="filterAufsichtsbereiche" removable autocomplete :model-value="undefined" />
				<svws-ui-multi-select v-if="wochentypen > 0" title="Wochentyp" :items="new Array(wochentypen)" :item-text="(i, ii)=> ii === 0 ? 'Allgemein' : String.fromCharCode(64 + ii) + ' Woche'" :model-value="0" ref="refWochentyp" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="add"> Pausenaufsicht hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import type { StundenplanPausenzeit, StundenplanPausenaufsicht, List } from "@core";
	import type { ComponentExposed } from "vue-component-type-helpers";
	import { StundenplanAufsichtsbereich, LehrerListeEintrag, ArrayList } from "@core";
	import { SvwsUiMultiSelect } from "@ui";
	import { ref } from "vue";

	const props = defineProps<{
		pausenzeit: StundenplanPausenzeit;
		listLehrer: List<LehrerListeEintrag>;
		listAufsichtsbereiche: List<StundenplanAufsichtsbereich>;
		wochentypen: number;
		addAufsichtUndBereich: (pausenaufsicht: Partial<StundenplanPausenaufsicht>) => Promise<void>;
	}>();

	const modal = ref();
	const refLehrer = ref<ComponentExposed<typeof SvwsUiMultiSelect<LehrerListeEintrag>> | null>(null);
	const refAufsichtsbereich = ref<ComponentExposed<typeof SvwsUiMultiSelect<StundenplanAufsichtsbereich>> | null>(null);
	const refWochentyp = ref<ComponentExposed<typeof SvwsUiMultiSelect<number>> | null>(null);

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
		if (!(refLehrer.value?.content instanceof LehrerListeEintrag))
			return;
		const bereiche = new ArrayList<number>();
		if (refAufsichtsbereich.value?.content instanceof StundenplanAufsichtsbereich)
			bereiche.add(refAufsichtsbereich.value.content.id);
		const wochentyp = typeof refWochentyp.value?.content === 'number' ? refWochentyp.value?.content : 0;
		await props.addAufsichtUndBereich({ idPausenzeit: props.pausenzeit.id, idLehrer: refLehrer.value.content.id, wochentyp, bereiche });
		modal.value.closeModal();
	}
</script>
