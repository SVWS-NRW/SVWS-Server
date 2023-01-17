<template>
	<i-ri-team-line v-if="props.routename === 'klassen'" />
	<i-ri-group-line v-else-if="props.routename === 'schueler'" />
	<i-ri-bar-chart-2-line v-else-if="props.routename === 'statistik'" />
	<i-ri-community-line v-else-if="props.routename === 'schule'" />
	<i-ri-archive-line v-else-if="props.routename === 'kataloge'" />
	<i-ri-briefcase-line v-else-if="props.routename === 'lehrer'" />
	<i-ri-slideshow-line v-else-if="props.routename === 'kurse'" />
	<IconGraduationCap v-else-if="props.routename === 'gost'" />
</template>

<script setup lang="ts">

	import IconGraduationCap from "../../images/IconGraduationCap.vue";

	const props = defineProps<{ routename: string }>();

</script>
