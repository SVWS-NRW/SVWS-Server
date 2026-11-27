<template>
	<div class="min-h-0 flex-1 overflow-auto p-3">
		<div class="grid grid-cols-2 gap-3">
			<div class="rounded border border-ui-10 p-3"><div class="text-xs text-ui-secondary">Lerngruppen</div><div class="text-xl font-semibold">{{ daten.lerngruppen }}</div></div>
			<div class="rounded border border-ui-10 p-3"><div class="text-xs text-ui-secondary">Mit Lehrkraft</div><div class="text-xl font-semibold">{{ daten.besetzteLerngruppen }}</div></div>
			<div class="rounded border border-ui-10 p-3"><div class="text-xs text-ui-secondary">Geplante Stunden</div><div class="text-xl font-semibold">{{ daten.geplanteStunden.toLocaleString('de-DE') }} h</div></div>
			<div class="rounded border border-ui-10 p-3"><div class="text-xs text-ui-secondary">Stunden ohne Lehrkraft</div><div class="text-xl font-semibold">{{ daten.offeneStunden.toLocaleString('de-DE') }} h</div></div>
		</div>
		<p class="mt-3 text-xs text-ui-secondary">Die Stunden zählen Lerngruppen mit mindestens einer zugeordneten Lehrkraft als besetzt. Mehrere Lehrkräfte erhöhen den Bedarf nicht.</p>
	</div>
</template>

<script setup lang="ts">
	import type { UvPlanungsStatistik } from './UvPlanungsManager';

	defineProps<{
		daten: UvPlanungsStatistik;
	}>();
</script>
