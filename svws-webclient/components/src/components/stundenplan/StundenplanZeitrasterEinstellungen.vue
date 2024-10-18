<template>
	<svws-ui-input-wrapper>
		Voreinstellungen:
		<svws-ui-text-input placeholder="Unterrichtsbeginn" :model-value="DateUtils.getStringOfUhrzeitFromMinuten(settings.defaultUnterrichtsbeginn)" @change="beginn => (beginn !== null) && updateSettings('Unterrichtsbeginn', DateUtils.gibMinutenOfZeitAsString(beginn))" />
		<svws-ui-input-number placeholder="Stundendauer" :model-value="settings.defaultStundendauer" @change="updateSettings('Stundendauer', $event)" :min="5" :max="1440" />
		<svws-ui-input-number placeholder="Pausenzeit Für Raumwechsel" :model-value="settings.defaultPausenzeitFuerRaumwechsel" @change="updateSettings('PausenzeitFuerRaumwechsel', $event)" :min="0" :max="1440" />
		<svws-ui-input-number placeholder="1. Pause nach Stunde" :model-value="settings.defaultVormittagspause1Nach" @change="updateSettings('Vormittagspause1Nach', $event)" :min="0" :max="99" />
		<svws-ui-input-number placeholder="1. Pause Dauer" :model-value="settings.defaultVormittagspause1Dauer" @change="updateSettings('Vormittagspause1Dauer', $event)" :min="0" :max="99" />
		<svws-ui-input-number placeholder="2. Pause nach Stunde" :model-value="settings.defaultVormittagspause2Nach" @change="updateSettings('Vormittagspause2Nach', $event)" :min="0" :max="99" />
		<svws-ui-input-number placeholder="2. Pause Dauer" :model-value="settings.defaultVormittagspause2Dauer" @change="updateSettings('Vormittagspause2Dauer', $event)" :min="0" :max="99" />
		<svws-ui-input-number placeholder="Mittagspause nach Stunde" :model-value="settings.defaultMittagspauseNach" @change="updateSettings('MittagspauseNach', $event)" :min="0" :max="99" />
		<svws-ui-input-number placeholder="Mittagspause Dauer" :model-value="settings.defaultMittagspauseDauer" @change="updateSettings('MittagspauseNach', $event)" :min="0" :max="99" />
		<slot />
	</svws-ui-input-wrapper>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { DateUtils } from "../../../../core/src/core/utils/DateUtils";
	import type { StundenplanManager } from "../../../../core/src/core/utils/stundenplan/StundenplanManager";
	import type { StundenplanKonfiguration } from "../../../../core/src/core/data/stundenplan/StundenplanKonfiguration";

	const props = defineProps<{
		manager: () => StundenplanManager;
		setSettingsDefaults?: (value: StundenplanKonfiguration) => Promise<void>;
	}>();

	const settings = computed(() => props.manager().stundenplanKonfigGet());

	async function updateSettings(action: string, value: number | null): Promise<void> {
		if (value === null)
			return;
		console.log(action)
		switch (action) {
			case 'Unterrichtsbeginn':
				props.manager().stundenplanSetDefaultUnterrichtsbeginn(value);
				break;
			case 'Stundendauer':
				props.manager().stundenplanSetDefaultStundendauer(value);
				break;
			case 'PausenzeitFuerRaumwechsel':
				props.manager().stundenplanSetDefaultPausenzeitFuerRaumwechsel(value);
				break;
			case 'Vormittagspause1Nach':
				props.manager().stundenplanSetDefaultVormittagspause1Nach(value);
				break;
			case 'Vormittagspause1Dauer':
				props.manager().stundenplanSetDefaultVormittagspause1Dauer(value);
				break;
			case 'Vormittagspause2Nach':
				props.manager().stundenplanSetDefaultVormittagspause2Nach(value);
				break;
			case 'Vormittagspause2Dauer':
				props.manager().stundenplanSetDefaultVormittagspause2Dauer(value);
				break;
			case 'MittagspauseNach':
				props.manager().stundenplanSetDefaultMittagspauseNach(value);
				break;
			case 'MittagspauseDauer':
				props.manager().stundenplanSetDefaultMittagspauseDauer(value);
				break;
			default:
				break;
		}
		if (props.setSettingsDefaults !== undefined)
			await props.setSettingsDefaults(props.manager().stundenplanKonfigGet());
	}

</script>