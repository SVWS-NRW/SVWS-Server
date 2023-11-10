<template>
	<svws-ui-content-card title="Zeitraster">
		Kommt noch
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, StundenplanZeitraster} from "@core";

	const props = defineProps<{
		listKatalogeintraege: () => List<StundenplanZeitraster>;
		patchZeitraster: (zeitraster : Iterable<StundenplanZeitraster>) => Promise<void>;
		addZeitraster: (zeitraster : Iterable<StundenplanZeitraster>) => Promise<void>;
		removeZeitraster: (zeitraster : Iterable<StundenplanZeitraster>) => Promise<void>;
	}>();

</script>
