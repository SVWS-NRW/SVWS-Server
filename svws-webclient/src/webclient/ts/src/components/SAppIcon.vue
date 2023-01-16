<template>
	<i-ri-team-line v-if="props.routename === 'klassen'" />
	<i-ri-group-line v-else-if="props.routename === 'schueler'" />
	<i-ri-bar-chart-2-line v-else-if="props.routename === 'statistik'" />
	<i-ri-community-line v-else-if="props.routename === 'schule'" />
	<i-ri-archive-line v-else-if="props.routename === 'kataloge'" />
	<i-ri-briefcase-line v-else-if="props.routename === 'lehrer'" />
	<i-ri-slideshow-line v-else-if="props.routename === 'kurse'" />
	<svg v-else-if="props.routename === 'gost'" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="1.2em" height="1.2em">
		<g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2">
			<path d="m2.573 8.463l8.659-4.329a.6.6 0 0 1 .536 0l8.659 4.33a.6.6 0 0 1 0 1.073l-8.659 4.329a.6.6 0 0 1-.536 0l-8.659-4.33a.6.6 0 0 1 0-1.073Z" />
			<path d="M22.5 13V9.5l-2-1m-16 2v5.412a2 2 0 0 0 1.142 1.807l5 2.374a2 2 0 0 0 1.716 0l5-2.374a2 2 0 0 0 1.142-1.807V10.5" />
		</g>
	</svg>
</template>

<script setup lang="ts">

	const props = defineProps<{ routename: string }>();

</script>
