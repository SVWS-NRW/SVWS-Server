<template>
	<svws-ui-content-card :title="`${item}. Stunde`">
		<svws-ui-input-wrapper>
			<svws-ui-text-input :model-value="item" type="number" required placeholder="Bezeichnung" @blur="patchStunde" />
			<svws-ui-button v-for="w of fehlendeZeitraster" :key="w.id" type="secondary" @click="addZeitraster(w, item)">{{ w.kuerzel }} {{ item }}. Stunde einfügen </svws-ui-button>
			<svws-ui-spacing v-if="fehlendeZeitraster.length" />
			<svws-ui-button type="danger" @click="removeZeitraster(stundenplanManager().getListZeitrasterZuStunde(item))"> <i-ri-delete-bin-line /> Stunde entfernen </svws-ui-button>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { StundenplanManager, StundenplanZeitraster, Wochentag } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		item: number;
		stundenplanManager: () => StundenplanManager;
		patchZeitraster: (data: Partial<StundenplanZeitraster>, zeitraster: StundenplanZeitraster) => Promise<void>;
		removeZeitraster: (multi: Iterable<StundenplanZeitraster>) => Promise<void>;
		addZeitraster: (wochentag: Wochentag | undefined, stunde : number | undefined) => Promise<void>;
	}>();

	async function patchStunde(event: string | number) {
		for (const zeitraster of props.stundenplanManager().getListZeitrasterZuStunde(props.item))
			await props.patchZeitraster({unterrichtstunde: Number(event)}, zeitraster);
	}

	const fehlendeZeitraster = computed<Wochentag[]>(()=> {
		const arr = [];
		for (const w of props.stundenplanManager().zeitrasterGetWochentageAlsEnumRange())
			if (props.stundenplanManager().zeitrasterGetByWochentagAndStundeOrNull(w.id, props.item) === null)
				arr.push(w);
		return arr;
	})
</script>
