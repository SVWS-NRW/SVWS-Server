<template>
	<div role="cell" class="data-table__td" :class="{'vorhanden' : selected && !aktiviert, 'nichtvorhanden' : !selected && !aktiviert, 'deaktiviert':aktiviert }" :title="aktiviert ? 'Bereits aktiviert durch Benutzergruppe' : ''">
		<svws-ui-checkbox v-model="selected" :disabled="aktiviert">
			{{ kompetenz.daten.bezeichnung }}
		</svws-ui-checkbox>
	</div>
	<div role="cell" class="data-table__td font-mono" :class="{'vorhanden' : selected && !aktiviert, 'nichtvorhanden' : !selected && !aktiviert, 'deaktiviert':aktiviert }" :title="aktiviert ? 'Bereits aktiviert durch Benutzergruppe' : ''">
		{{ kompetenz.daten.id }}
	</div>
</template>

<script setup lang="ts">

	import {BenutzergruppenManager, BenutzerKompetenz } from "@svws-nrw/svws-core";
	import { computed, ComputedRef, WritableComputedRef } from "vue";

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
.vorhanden {
	@apply bg-primary/5 text-primary;
}

.nichtvorhanden {
	@apply bg-white;
}

.deaktiviert {
	@apply text-black/50;
}

.data-table__td-content {
	@apply inline-block;
}
</style>
