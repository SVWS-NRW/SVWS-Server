<template>
	<div v-if="visible" class="app-container">
		<s-card-schueler-halbjahr :data="data!" @patch="patch" />
		<s-card-schueler-zeugnis-abschluss :data="data!" @patch="patch" @patch-bemerkungen="patchBemerkungen" />
		<s-card-schueler-abschluss-berechnung :data="data!" @patch="patch" />
		<s-card-schueler-fehlzeiten :data="data!" @patch="patch" />
	</div>
</template>

<script setup lang="ts">

	import { SchuelerLernabschnittBemerkungen, SchuelerLernabschnittsdaten } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	const props = defineProps<{
		data: SchuelerLernabschnittsdaten | undefined;
		patch: (data : Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
		patchBemerkungen: (data : Partial<SchuelerLernabschnittBemerkungen>) => Promise<void>;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return (props.data !== undefined);
	});

</script>
