<template>
	<svws-ui-modal :show @update:show="emit('close-modal')">
		<template #modalTitle>Einen zweiten Erziehungsberechtigten hinzufügen</template>
		<template #modalContent>
			<schueler-erziehungsberechtigte-zweiter-erz-felder :model :schuljahr :readonly />
			<div v-if="hatKompetenzUpdate" class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="emit('close-modal')">Abbrechen</svws-ui-button>
				<svws-ui-button @click="save" :disabled="model.hatFehler()">
					Zweiten Erzieher speichern
				</svws-ui-button>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { computed } from "vue";
	import { ErzieherStammdatenModelProxy } from "~/components/schueler/erziehungsberechtigte/modelproxy/ErzieherStammdatenModelProxy";
	import SchuelerErziehungsberechtigteZweiterErzFelder from "./SchuelerErziehungsberechtigteZweiterErzFelder.vue";
	import type { Erzieherart } from "@core/core/data/erzieher/Erzieherart";
	import type { ErzieherStammdaten } from "@core/core/data/erzieher/ErzieherStammdaten";

	const props = defineProps<{
		show: boolean;
		zweiterErz: ErzieherStammdaten;
		pos2SourceId: number;
		erzieherartenById: Map<number, Erzieherart>;
		schuljahr: number;
		hatKompetenzUpdate: boolean;
		patchErzieherAnPosition: (data: Partial<ErzieherStammdaten>, id: number, pos: number) => Promise<void>;
	}>();

	const emit = defineEmits<{ 'close-modal': [] }>();

	const readonly = computed(() => !props.hatKompetenzUpdate);

	const model = new ErzieherStammdatenModelProxy(
		() => props.zweiterErz,
		() => props.erzieherartenById,
		() => props.schuljahr
	);

	async function save() {
		const { id, idSchueler, erhaeltAnschreiben, ...data } = model.proxy;
		await props.patchErzieherAnPosition(data, props.pos2SourceId, 2);
		emit('close-modal');
	}

</script>
