<template>
	<template v-if="hat_blockung">
		<router-view :key="$route.hash" />
	</template>
	<div v-else>
		Es liegt noch keine Planung für dieses Halbjahr vor. Klicken Sie auf
		"Blockung hinzufügen", um eine neue Kursplanung zu erstellen.
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { ListKursblockungen } from "~/apps/gost/ListKursblockungen";

	const props = defineProps<{
		listBlockungen: ListKursblockungen;
	}>();

	const hat_blockung: ComputedRef<boolean> = computed(() => props.listBlockungen.liste.length > 0);

</script>
