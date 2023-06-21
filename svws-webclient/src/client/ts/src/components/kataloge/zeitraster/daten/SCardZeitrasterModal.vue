<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Zeitraster bearbeiten</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<template v-if="multi.length > 1">
					{{ Wochentag.fromIDorException(tageRange.at(0) || 1) }} bis {{ Wochentag.fromIDorException(tageRange.at(-1)||1) }}
				</template>
				<template v-if="item.id < 1">
					<svws-ui-checkbox v-for="tag of [1,2,3,4,5,6,7]" :key="tag" :model-value="listTage.get(tag) || false" @update:model-value="updateMap(tag, $event)" :value="tag">{{ Wochentag.fromIDorException(tag) }}</svws-ui-checkbox>
				</template>
				<div v-if="multi.length === 1 && item.id > 0" class="font-bold">{{ Wochentag.fromIDorException(item.wochentag > 0 ? item.wochentag : 1) }}</div>
				<svws-ui-text-input type="number" v-model="item.unterrichtstunde" required placeholder="Stunde" />
				<svws-ui-text-input v-model="item.stundenbeginn" required placeholder="Stundenbeginn" />
				<svws-ui-text-input v-model="item.stundenende" placeholder="Stundenende" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.unterrichtstunde"> Zeitraster Anpassen </svws-ui-button>
			<svws-ui-button v-if="removeZeitraster" type="secondary" @click="remove()"> Zeitraster entfernen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import type { List} from "@core";
	import { StundenplanZeitraster, Wochentag } from "@core";
	import { computed, ref, shallowRef } from "vue";

	const props = defineProps<{
		listKatalogeintraege: () => List<StundenplanZeitraster>;
		patchZeitraster: (daten: StundenplanZeitraster, multi: StundenplanZeitraster[]) => Promise<void>;
		addZeitraster: (daten: StundenplanZeitraster, tage: number[]) => Promise<void>;
		removeZeitraster?: (multi: StundenplanZeitraster[]) => Promise<void>;
	}>();

	const modal = ref();
	const item = ref<StundenplanZeitraster>(new StundenplanZeitraster());
	const multi = shallowRef<StundenplanZeitraster[]>([]);

	function updateMap(id: number, ok: any) {
		if (typeof ok === 'boolean')
			listTage.value = listTage.value.set(id, ok);
	}

	const range = (x: number,y: number) => Array.from((function*(){ while (x <= y) yield x++ })());
	const tageRange = computed(() => {
		let min = 0;
		let max = 0;
		for (const e of props.listKatalogeintraege()) {
			if (e.wochentag < min || min === 0)
				min = e.wochentag;
			if (e.wochentag > max)
				max = e.wochentag;
		}
		return min > 0 && max > 0 ? range(min, max) : [1,2,3,4,5];
	});

	const map = ref(new Map<number, boolean>());
	const listTage = computed({
		get: () => {
			for (const tag of tageRange.value)
				map.value.set(tag, true);
			return map.value;
		},
		set: (val) => {
			map.value = val;
		}
	})

	const openModal = (zeitraster?: StundenplanZeitraster, 	m?: boolean) => {
		item.value = zeitraster || new StundenplanZeitraster();
		const a = [];
		if (m === true && zeitraster) {
			for (const z of props.listKatalogeintraege())
				if (z.unterrichtstunde === zeitraster.unterrichtstunde && z.stundenbeginn === zeitraster.stundenbeginn && z.stundenende === zeitraster.stundenende)
					a.push(z);
		} else
			a.push(item.value);
		multi.value = a;
		modal.value.openModal();
	}

	async function importer() {
		if (item.value.id > 0) {
			await props.patchZeitraster(item.value, multi.value)
		} else {
			const list = [];
			for (const [tag, ok] of listTage.value.entries())
				if (ok === true)
					list.push(tag);
			await props.addZeitraster(item.value, list);
		}
		modal.value.closeModal();
	}

	async function remove() {
		if (item.value.id > 0 && props.removeZeitraster) {
			await props.removeZeitraster(multi.value);
		}
		modal.value.closeModal();
	}
</script>
