<template>
	<svws-ui-table-cell :class="{'vorhanden' : selected && !aktiviert, 'nichtvorhanden' : !selected && !aktiviert, 'deaktiviert':aktiviert }" :title="aktiviert ? 'Bereits aktiviert durch Benutzergruppe' : ''">
		<svws-ui-checkbox v-model="selected" :disabled="aktiviert">
			{{ kompetenz.daten.bezeichnung }}
		</svws-ui-checkbox>
	</svws-ui-table-cell>
	<svws-ui-table-cell class="font-mono" :class="{'vorhanden' : selected && !aktiviert, 'nichtvorhanden' : !selected && !aktiviert, 'deaktiviert':aktiviert }" :title="aktiviert ? 'Bereits aktiviert durch Benutzergruppe' : ''">
		{{ kompetenz.daten.id }}
	</svws-ui-table-cell>
</template>

<script setup lang="ts">

	import type {BenutzergruppenManager, BenutzerKompetenz } from "@core";
	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		getBenutzergruppenManager: () => BenutzergruppenManager;
		kompetenz: BenutzerKompetenz;
		istAdmin: boolean;
		addKompetenz : (kompetenz: BenutzerKompetenz) => Promise<boolean>;
		removeKompetenz : (kompetenz: BenutzerKompetenz) => Promise<boolean>;
	}>();

	const aktiviert : ComputedRef<boolean> = computed(() => props.istAdmin);

	const selected: WritableComputedRef<boolean> = computed({
		get: () => props.getBenutzergruppenManager().hatKompetenz(props.kompetenz),
		set: (value) => {
			const alt = props.getBenutzergruppenManager().hatKompetenz(props.kompetenz);
			if (alt === value)
				return;
			if (value)
				void props.addKompetenz(props.kompetenz);
			else
				void props.removeKompetenz(props.kompetenz);
		}
	});

</script>

<style scoped lang="postcss">
.deaktiviert {
	@apply text-black/50;
}

.data-table__td-content {
	@apply inline-block;
}
</style>
