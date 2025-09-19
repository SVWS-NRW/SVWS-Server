<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="small" class="hidden">
		<template #modalTitle>Ungültige Regeln</template>
		<template #modalDescription>
			Sollen folgende ungültige Regeln entfernt werden? Für die Blockung spielen diese keine Rolle mehr.
			<svws-ui-table selectable v-model="selected" :items="manager().regelGetMapUngueltig().values()" disable-footer :columns="[{key: 'typ', label: 'Typ',}, {key: 'id', label: 'Beschreibung'}]">
				<template #cell(typ)="{value}">
					{{ GostKursblockungRegelTyp.fromTyp(value).bezeichnung }}
				</template>
				<template #cell(id)="{value}">
					{{ manager().regelGetMapUngueltigBeschreibung().get(value) }}
				</template>
			</svws-ui-table>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="removeRegeln">OK</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { shallowRef } from 'vue';
	import type { GostBlockungsdatenManager, GostBlockungRegel } from '@core';
	import { GostBlockungRegelUpdate, GostKursblockungRegelTyp } from '@core';

	const props = defineProps<{
		manager: () => GostBlockungsdatenManager;
		regelnUpdate: (update: GostBlockungRegelUpdate) => Promise<void>;
	}>();

	const selected = shallowRef<GostBlockungRegel[]>([]);
	const show = shallowRef<boolean>(false);

	async function removeRegeln() {
		const update = new GostBlockungRegelUpdate();
		for (const regel of selected.value)
			update.listEntfernen.add(regel);
		show.value = false;

		if (!update.listEntfernen.isEmpty())
			await props.regelnUpdate(update);
		selected.value = [];
	}

	const openModal = () => {
		selected.value = [...props.manager().regelGetMapUngueltig().values()];
		show.value = true;
	}

</script>
