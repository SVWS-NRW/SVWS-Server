<template>
	<template v-if="hatBlockung">
		<router-view :key="$route.hash" />
	</template>
	<div v-else>
		Es liegt noch keine Planung für dieses Halbjahr vor. Klicken Sie auf
		"Blockung hinzufügen", um eine neue Kursplanung zu erstellen.
	</div>
</template>

<script setup lang="ts">

	defineProps<{
		hatBlockung: boolean;
	}>();

</script>
