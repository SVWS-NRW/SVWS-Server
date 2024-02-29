<template>
	<i-ri-team-line v-if="props.routename === 'klassen'" />
	<i-ri-group-line v-else-if="props.routename === 'schueler'" />
	<i-ri-bar-chart-2-line v-else-if="props.routename === 'statistik'" />
	<i-ri-calendar-event-line v-else-if="props.routename === 'stundenplan'" />
	<i-ri-school-line v-else-if="props.routename === 'schule'" />
	<i-ri-archive-line v-else-if="props.routename === 'kataloge'" />
	<i-ri-briefcase-line v-else-if="props.routename === 'lehrer'" />
	<i-ri-book-2-line v-else-if="props.routename === 'kurse'" />
	<i-ri-graduation-cap-line v-else-if="props.routename === 'gost'" />
	<i-ri-archive-stack-line v-else-if="props.routename === 'schema'" />
</template>

<script setup lang="ts">
	const props = defineProps<{ routename: string }>();
</script>
