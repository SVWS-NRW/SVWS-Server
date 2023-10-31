<template>
	<svws-ui-content-card :title="`${item}. Stunde`">
		<svws-ui-input-wrapper>
			<svws-ui-input-number :model-value="item" type="number" required placeholder="Bezeichnung" @change="patchStunde" />
			<svws-ui-button v-for="w of fehlendeZeitraster" :key="w.id" type="secondary" @click="add(w, item)">{{ w.kuerzel }} {{ item }}. Stunde einfügen </svws-ui-button>
			<svws-ui-spacing v-if="fehlendeZeitraster.length" />
			<svws-ui-button type="danger" @click="removeZeitraster(stundenplanManager().getListZeitrasterZuStunde(item))"> <i-ri-delete-bin-line /> Stunde entfernen </svws-ui-button>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { StundenplanZeitraster, StundenplanManager, Wochentag} from "@core";
	import { ArrayList } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		item: number;
		stundenplanManager: () => StundenplanManager;
		patchZeitraster: (zeitraster : Iterable<StundenplanZeitraster>) => Promise<void>;
		removeZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
		addZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
	}>();

	async function patchStunde(stunde: number | null) {
		if (stunde === null)
			return;
		const list = new ArrayList<StundenplanZeitraster>();
		for (const zeitraster of props.stundenplanManager().getListZeitrasterZuStunde(props.item)) {
			zeitraster.unterrichtstunde = stunde;
			list.add(zeitraster);
		}
		await props.patchZeitraster(list);
	}

	async function add(w: Wochentag, stunde: number) {
		const list = props.stundenplanManager().zeitrasterGetDummyListe(w.id, w.id, stunde, stunde);
		await props.addZeitraster(list);
	}

	const fehlendeZeitraster = computed<Wochentag[]>(()=> {
		const arr = [];
		for (const w of props.stundenplanManager().zeitrasterGetWochentageAlsEnumRange())
			if (props.stundenplanManager().zeitrasterGetByWochentagAndStundeOrNull(w.id, props.item) === null)
				arr.push(w);
		return arr;
	})
</script>
