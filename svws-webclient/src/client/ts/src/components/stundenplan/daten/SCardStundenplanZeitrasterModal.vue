<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Zeitraster bearbeiten</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<template v-if="multi.length > 1">
					<svws-ui-checkbox v-for="tag of tageRange" :key="tag" :model-value="listTage.get(tag)" @update:model-value="updateMap(tag, $event)" :value="tag">{{ Wochentag.fromIDorException(tag) }}</svws-ui-checkbox>
				</template>
				<div v-else class="font-bold">{{ Wochentag.fromIDorException(item.wochentag) }}</div>
				<svws-ui-text-input type="number" v-model="item.unterrichtstunde" required placeholder="Stunde" />
				<svws-ui-text-input v-model="item.stundenbeginn" required placeholder="Stundenbeginn" />
				<svws-ui-text-input v-model="item.stundenende" placeholder="Stundenende" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.unterrichtstunde"> Zeitraster Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import type { StundenplanManager} from "@core";
	import { StundenplanZeitraster, Wochentag } from "@core";
	import { computed, ref, shallowRef } from "vue";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
		patchZeitraster: (daten: StundenplanZeitraster, multi: StundenplanZeitraster[]) => Promise<void>;
		addZeitraster: (daten: StundenplanZeitraster, tage: number[]) => Promise<void>;
	}>();

	const modal = ref();
	const item = ref<StundenplanZeitraster>(new StundenplanZeitraster());
	const multi = shallowRef<StundenplanZeitraster[]>([]);


	function updateMap(id: number, ok: any) {
		if (typeof ok === 'boolean')
			listTage.value.set(id, ok);
	}

	const range = (x: number,y: number) => Array.from((function*(){ while (x <= y) yield x++ })());
	const tageRange = computed(() => range(props.stundenplanManager().getZeitrasterWochentagMin().id, props.stundenplanManager().getZeitrasterWochentagMax().id));

	const listTage = ref(new Map<number, boolean>());
	for (const tag of tageRange.value)
		listTage.value.set(tag, true);

	const openModal = (zeitraster?: StundenplanZeitraster, 	m?: boolean) => {
		if (zeitraster)
			item.value = zeitraster;
		if (m === true && zeitraster) {
			multi.value = [];
			for (const z of props.stundenplanManager().getListZeitraster())
				if (z.unterrichtstunde === zeitraster.unterrichtstunde && z.stundenbeginn === zeitraster.stundenbeginn && z.stundenende === zeitraster.stundenende)
					multi.value.push(z);
		} else
			multi.value = [item.value];
		modal.value.openModal();
	}

	async function importer() {
		item.value
			? await props.patchZeitraster(item.value, multi.value)
			: await props.addZeitraster(item.value, [...listTage.value.keys()]);
		modal.value.closeModal();
	}
</script>
