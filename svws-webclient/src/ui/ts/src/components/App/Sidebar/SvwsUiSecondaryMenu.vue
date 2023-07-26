<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>
				<slot name="headline" />
			</h1>
			<div v-if="$slots.abschnitt" class="input--schule-abschnitte">
				<slot name="abschnitt" />
			</div>
		</div>
		<div class="secondary-menu--header">
			<slot name="header" />
		</div>
		<div class="secondary-menu--content">
			<slot name="content" />
		</div>
	</div>
</template>
<script setup lang="ts">
</script>

<style lang="postcss">
.secondary-menu--headline {
	@apply py-2 text-headline;
	@apply relative z-20;
}

.app--sidebar-container .v-table th:last-child,
.app--sidebar-container .v-table td:last-child {
	@apply border-r-0;
}

.secondary-menu--header {
	@apply flex-shrink-0;
	@apply relative z-10;
}

.secondary-menu--content {
	@apply flex-grow;
	@apply overflow-y-auto;
	@apply relative z-0;
	@apply flex flex-col;
	margin-bottom: -1px;
}

.secondary-menu--content .secondary-menu--navigation {
	@apply flex flex-col items-start
}

.secondary-menu--navigation + .secondary-menu--navigation {
	@apply mt-5 pt-5;
}

.secondary-menu--navigation--headline {
	@apply text-headline-md px-3 mb-2;
}

.input {
	@apply cursor-pointer;
}

.input--schule-abschnitte {
	@apply -mt-0.5;
}

.input--schule-abschnitte .dropdown-icon {
	@apply pl-0;
	font-size: 0.8rem;
}

.input--schule-abschnitte .dropdown-icon:hover span.icon,
.input--schule-abschnitte .dropdown-icon:focus span.icon {
	@apply bg-transparent text-black;
}

.input--schule-abschnitte input.text-input--control {
	@apply border-0 p-0 h-auto;
	@apply font-bold;
	width: 8ch;
	padding-right: 1.4rem !important;
}

.input--schule-abschnitte input.text-input--control,
.input--schule-abschnitte .text-input--readonly input.text-input--control {
	@apply cursor-pointer;
}

.input--schule-abschnitte .multiselect-input-component {
	@apply cursor-pointer opacity-40;

	&:hover,
	&.with-open-list,
	&--danger {
		@apply opacity-100;
	}
}

.input--schule-abschnitte .multiselect--items-wrapper {
	@apply mt-2;
	min-width: 14ch;
}

.secondary-menu--breadcrumbs {
	@apply flex flex-wrap;

	> *:not(:last-child) {
		@apply opacity-25 cursor-pointer;

		&:hover {
			@apply opacity-100 text-primary;

			&:after {
				@apply opacity-25;
			}
		}

		&:after {
			@apply inline-block mx-1 text-black dark:text-white;
			content: '/';
		}
	}
}
</style>
