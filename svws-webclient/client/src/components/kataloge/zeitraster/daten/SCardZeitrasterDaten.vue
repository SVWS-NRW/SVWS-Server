<template>
	<svws-ui-content-card title="Zeitraster">
		Kommt noch
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, StundenplanZeitraster} from "@core";

	const props = defineProps<{
		listKatalogeintraege: () => List<StundenplanZeitraster>;
		patchZeitraster: (daten: StundenplanZeitraster, multi: StundenplanZeitraster[]) => Promise<void>;
		addZeitraster: (daten: StundenplanZeitraster, tage: number[]) => Promise<void>;
		removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
	}>();

</script>
