<template>
	<div class="relative inline-flex flex-col justify-center mx-auto">
		<!-- Link zum Benutzer	-->
		<a class="app--menu--initials" href="#" @click.prevent="onClick">
			<svws-ui-tooltip position="right" v-if="user" :indicator="false">
				<div class="app--menu--initials--icon" :class="{'svws-is-admin-client': isAdminClient}">
					<template v-if="user.length > 5">{{ user.split(' ').map((username) => username[0]).join('').toUpperCase() }}</template>
					<template v-else>{{ user.slice(0, 2).toUpperCase() }}</template>
				</div>
				<template #content>
					<div class="app--menu--initials--label">
						Angemeldet als {{ user }}
						<template v-if="schule">
							<br>
							<span class="opacity-50">{{ schule }}</span>
						</template>
						<template v-if="schema">
							<br>
							<span class="opacity-50">DB: {{ schema }}</span>
						</template>
					</div>
				</template>
			</svws-ui-tooltip>
			<!-- Hinweise am Nutzer (u.a. Anzahl offener Wiedervorlagen)	-->
			<span v-if="props.hint" class="absolute right-0 top-0 translate-x-1/2 -translate-y-1/4">
				<svws-ui-badge :type="props.hint.type ?? 'highlight'"
					class="max-w-[25px] break-keep"
					:title="props.hint.text"
					size="big"
					rounded>
					{{ formatNumber(props.hint.number) }}
				</svws-ui-badge>
			</span>
		</a>
	</div>
</template>

<script setup lang='ts'>
	import type { Type } from '../../types';

	export type HintType = {
		number: number;
		type: Type;
		text: string;
	};

	const props = withDefaults(defineProps<{
		collapsed?: boolean;
		user?: string;
		schule?: string;
		schema?: string;
		isAdminClient?: boolean;
		hint?: HintType
	}>(), {
		collapsed: false,
		user: undefined,
		schule: undefined,
		schema: undefined,
		isAdminClient: false,
		hint: undefined,
	});

	const emit = defineEmits<{
		(e: 'click', event: MouseEvent): void;
	}>();

	function onClick(event: MouseEvent) {
		emit("click", event);
	}

	function formatNumber(value: number): string {
		if (!Number.isInteger(value)) {
			throw new Error('value muss eine ganze Zahl sein.');
		}

		return Math.abs(value) >= 100 ? '99+' : String(value);
	}
</script>
