<template>
	<svws-ui-content-card :title="`${item}. Stunde`">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(first.stundenbeginn ?? 0)" required placeholder="Stundenbeginn" @blur="start = $event " />
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(first.stundenende ?? 0)" placeholder="Stundenende" @blur="ende = $event" />
			<svws-ui-button v-if="start !== null || ende !== null" type="secondary" @click="patchZeiten"> Stundenzeiten aktualisieren </svws-ui-button>
			<div class="col-span-full">
				<svws-ui-input-number :model-value="item" required placeholder="Bezeichnung" @change="patchStunde" />
			</div>
			<div class="col-span-full">
				<svws-ui-button v-for="w of fehlendeZeitraster" :key="w.id" type="secondary" @click="add(w, item)">{{ w.kuerzel }} {{ item }}. Stunde einfügen </svws-ui-button>
				<svws-ui-spacing v-if="fehlendeZeitraster.length" />
			</div>
			<div class="col-span-full">
				<svws-ui-button type="danger" @click="removeZeitraster(zeitraster)"> <i-ri-delete-bin-line /> Stunde entfernen </svws-ui-button>
			</div>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { StundenplanManager, Wochentag } from "@core";
	import { StundenplanZeitraster } from "@core";
	import { ArrayList, DateUtils } from "@core";
	import { computed, ref } from "vue";

	const props = defineProps<{
		item: number;
		stundenplanManager: () => StundenplanManager;
		patchZeitraster: (zeitraster : Iterable<StundenplanZeitraster>) => Promise<void>;
		removeZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
		addZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
	}>();

	const zeitraster = computed(() => props.stundenplanManager().getListZeitrasterZuStunde(props.item))

	const first = computed(()=>{
		if (zeitraster.value.size() < 1)
			return new StundenplanZeitraster();
		return zeitraster.value.get(0);

	})
	const start = ref<string|null>(null);
	const ende = ref<string|null>(null);

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

	async function patchZeiten() {
		if (start.value === null || ende.value === null)
			return;
		const list = new ArrayList<StundenplanZeitraster>();
		for (const zeitraster of props.stundenplanManager().getListZeitrasterZuStunde(props.item)) {
			const stundenbeginn = DateUtils.gibMinutenOfZeitAsString(start.value);
			const stundenende = DateUtils.gibMinutenOfZeitAsString(ende.value);
			zeitraster.stundenbeginn = stundenbeginn;
			zeitraster.stundenende = stundenende;
			list.add(zeitraster);
		}
		start.value = null;
		ende.value = null;
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
