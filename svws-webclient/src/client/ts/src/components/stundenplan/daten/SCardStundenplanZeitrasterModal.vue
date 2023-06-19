<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Zeitraster bearbeiten</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<template v-if="multi.length > 1">
					{{ stundenplanManager().getZeitrasterWochentagMin().beschreibung }}	bis {{ stundenplanManager().getZeitrasterWochentagMax().beschreibung }}
				</template>
				<template v-if="item.id < 1">
					<svws-ui-checkbox v-for="tag of tageRange" :key="tag" :model-value="listTage.get(tag)" @update:model-value="updateMap(tag, $event)" :value="tag">{{ Wochentag.fromIDorException(tag) }}</svws-ui-checkbox>
				</template>
				<div v-if="multi.length === 1 && item.id > 0" class="font-bold">{{ Wochentag.fromIDorException(item.wochentag) }}</div>
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
	import type { StundenplanManager} from "@core";
	import { StundenplanZeitraster, Wochentag } from "@core";
	import { computed, ref, shallowRef } from "vue";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
		patchZeitraster: (daten: StundenplanZeitraster, multi: StundenplanZeitraster[]) => Promise<void>;
		addZeitraster: (daten: StundenplanZeitraster, tage: number[]) => Promise<void>;
		removeZeitraster?: (multi: StundenplanZeitraster[]) => Promise<void>;
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
