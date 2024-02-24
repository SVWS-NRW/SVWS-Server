<template>
	<svws-ui-modal :show="showModal" size="small" class="hidden">
		<template #modalTitle>Regel erstellen</template>
		<template #modalContent>
			<div class="flex flex-col gap-1">
				<svws-ui-select v-model="kursart" :items="GostKursart.values()" :item-text="i => i.beschreibung" />
				<span class="mt-1" v-if="von.nummer !== bis.nummer">von Schiene {{ von.nummer }} bis Schiene {{ bis.nummer }}</span>
				<span class="mt-1" v-else>in Schiene {{ von.nummer }}</span>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="regel_hinzufuegen(true)"><i-ri-lock-line /> Sperren</svws-ui-button>
			<svws-ui-button type="secondary" @click="regel_hinzufuegen(false)">Alleine</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref, shallowRef } from "vue";
	import type { GostBlockungRegelUpdate, GostBlockungsergebnisManager } from "@core";
	import { GostKursart, GostBlockungSchiene } from "@core";

	const props = defineProps<{
		getErgebnisManager: () => GostBlockungsergebnisManager;
		regelnUpdate: (update: GostBlockungRegelUpdate) => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const von = ref<GostBlockungSchiene>(new GostBlockungSchiene());
	const bis = ref<GostBlockungSchiene>(new GostBlockungSchiene());

	const openModal = (schieneVon: GostBlockungSchiene, schieneBis: GostBlockungSchiene) => {
		von.value = schieneVon;
		bis.value = schieneBis;
		showModal().value = true;
	};

	const kursart = shallowRef<GostKursart>(GostKursart.GK)

	async function regel_hinzufuegen(value: boolean) {
		showModal().value = false;
		const update = value === true
			? props.getErgebnisManager().regelupdateCreate_01_KURSART_SPERRE_SCHIENEN_VON_BIS(kursart.value.id, von.value.nummer, bis.value.nummer)
			: props.getErgebnisManager().regelupdateCreate_06_KURSART_ALLEIN_IN_SCHIENEN_VON_BIS(kursart.value.id, von.value.nummer, bis.value.nummer)
		await props.regelnUpdate(update);
	}

	defineExpose({ openModal });

</script>
