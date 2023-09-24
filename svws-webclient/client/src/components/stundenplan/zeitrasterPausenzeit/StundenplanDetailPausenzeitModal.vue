<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal">
		<template #modalTitle>Pausenaufsicht hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-multi-select title="Aufsichtführende Lehrkraft" :items="listLehrer"
					:item-text="(i: LehrerListeEintrag)=>`${i.kuerzel} (${i.vorname} ${i.nachname})`"
					:item-filter="filter" removable autocomplete :model-value="undefined" ref="refLehrer" />
				<svws-ui-multi-select title="Aufsichtsbereiche" :items="listAufsichtsbereiche" tags
					:item-text="(i: StundenplanAufsichtsbereich)=>i.beschreibung" ref="refAufsichtsbereich"
					:item-filter="filterAufsichtsbereiche" removable autocomplete :model-value="undefined" />
				<svws-ui-multi-select v-if="wochentypen > 0" title="Wochentyp" :items="wochentypenArray" :model-value="wochentypenArray[0]" :item-text="(i: WT)=>i?.text || 'leer'" ref="refWochentyp" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="add"> Pausenaufsicht hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { StundenplanPausenzeit, StundenplanPausenaufsicht, List , StundenplanAufsichtsbereich} from "@core";
	import type { ComponentExposed } from "vue-component-type-helpers";
	import { LehrerListeEintrag, ArrayList } from "@core";
	import { SvwsUiMultiSelect } from "@ui";
	import { computed, ref } from "vue";

	type WT = { typ: number; text: string }

	const props = defineProps<{
		pausenzeit: StundenplanPausenzeit;
		listLehrer: List<LehrerListeEintrag>;
		listAufsichtsbereiche: List<StundenplanAufsichtsbereich>;
		wochentypen: number;
		addAufsichtUndBereich: (pausenaufsicht: Partial<StundenplanPausenaufsicht>) => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const refLehrer = ref<ComponentExposed<typeof SvwsUiMultiSelect<LehrerListeEintrag>> | null>(null);
	const refAufsichtsbereich = ref<ComponentExposed<typeof SvwsUiMultiSelect<StundenplanAufsichtsbereich>> | null>(null);
	const refWochentyp = ref<ComponentExposed<typeof SvwsUiMultiSelect<WT>> | null>(null);

	const wochentypenArray = computed<WT[]>(() => {
		if (props.wochentypen < 1)
			return [];
		const a = new Array<WT>();
		for (let i = 0; i < props.wochentypen+1; i++)
			a.push({ typ: i, text: i === 0 ? 'Allgemein' : String.fromCharCode(64 + i) + ' Woche'});
		return a
	})

	const filter = (items: LehrerListeEintrag[], search: string) => {
		return items.filter(i => (i.istSichtbar === true) && (i.kuerzel.includes(search.toLocaleLowerCase()) || i.nachname?.toLocaleLowerCase().includes(search.toLocaleLowerCase())));
	};

	const filterAufsichtsbereiche = (items: StundenplanAufsichtsbereich[], search: string) => {
		return items.filter(i => (i.beschreibung) && (i.beschreibung.includes(search.toLocaleLowerCase()) || i.kuerzel?.toLocaleLowerCase().includes(search.toLocaleLowerCase())));
	};

	const openModal = () => {
		showModal().value = true;
	}

	async function add() {
		if (!(refLehrer.value?.content instanceof LehrerListeEintrag))
			return;
		const bereiche = new ArrayList<number>();
		if (refAufsichtsbereich.value?.content && Array.isArray(refAufsichtsbereich.value.content))
			for (const aufsichtsbereich of refAufsichtsbereich.value.content)
				bereiche.add(aufsichtsbereich.id);
		const wochentyp = refWochentyp.value?.content && 'typ' in refWochentyp.value.content ? refWochentyp.value.content.typ : 0;
		await props.addAufsichtUndBereich({ idPausenzeit: props.pausenzeit.id, idLehrer: refLehrer.value.content.id, wochentyp, bereiche });
		showModal().value = false;
	}

</script>
